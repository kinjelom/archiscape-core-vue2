import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { maxLength, required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import ProjectService from '@/entities/project/project.service';
import { IProject } from '@/shared/model/project.model';

import ProjectElementService from '@/entities/project-element/project-element.service';
import { IProjectElement } from '@/shared/model/project-element.model';

import { IProjectView, ProjectView } from '@/shared/model/project-view.model';
import ProjectViewService from './project-view.service';
import { C4ViewLevel } from '@/shared/model/enumerations/c-4-view-level.model';

const validations: any = {
  projectView: {
    name: {
      maxLength: maxLength(50),
    },
    documentation: {
      maxLength: maxLength(2048),
    },
    c4level: {
      required,
    },
    extElementId: {
      maxLength: maxLength(100),
    },
    image: {},
    project: {
      required,
    },
    projectElement: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class ProjectViewUpdate extends mixins(JhiDataUtils) {
  @Inject('projectViewService') private projectViewService: () => ProjectViewService;
  @Inject('alertService') private alertService: () => AlertService;

  public projectView: IProjectView = new ProjectView();

  @Inject('projectService') private projectService: () => ProjectService;

  public projects: IProject[] = [];

  @Inject('projectElementService') private projectElementService: () => ProjectElementService;

  public projectElements: IProjectElement[] = [];
  public c4ViewLevelValues: string[] = Object.keys(C4ViewLevel);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.projectViewId) {
        vm.retrieveProjectView(to.params.projectViewId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.projectView.id) {
      this.projectViewService()
        .update(this.projectView)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('archiscapeCoreApp.projectView.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.projectViewService()
        .create(this.projectView)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('archiscapeCoreApp.projectView.created', { param: param.id });
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public retrieveProjectView(projectViewId): void {
    this.projectViewService()
      .find(projectViewId)
      .then(res => {
        this.projectView = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public clearInputImage(field, fieldContentType, idInput): void {
    if (this.projectView && field && fieldContentType) {
      if (Object.prototype.hasOwnProperty.call(this.projectView, field)) {
        this.projectView[field] = null;
      }
      if (Object.prototype.hasOwnProperty.call(this.projectView, fieldContentType)) {
        this.projectView[fieldContentType] = null;
      }
      if (idInput) {
        (<any>this).$refs[idInput] = null;
      }
    }
  }

  public initRelationships(): void {
    this.projectService()
      .retrieve()
      .then(res => {
        this.projects = res.data;
      });
    this.projectElementService()
      .retrieve()
      .then(res => {
        this.projectElements = res.data;
      });
  }
}

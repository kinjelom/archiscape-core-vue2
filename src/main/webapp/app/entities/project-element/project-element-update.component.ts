import { Component, Vue, Inject } from 'vue-property-decorator';

import { maxLength, required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import ProjectService from '@/entities/project/project.service';
import { IProject } from '@/shared/model/project.model';

import ElementConfigurationService from '@/entities/element-configuration/element-configuration.service';
import { IElementConfiguration } from '@/shared/model/element-configuration.model';

import ProjectViewService from '@/entities/project-view/project-view.service';
import { IProjectView } from '@/shared/model/project-view.model';

import { IProjectElement, ProjectElement } from '@/shared/model/project-element.model';
import ProjectElementService from './project-element.service';
import { C4ElementType } from '@/shared/model/enumerations/c-4-element-type.model';

const validations: any = {
  projectElement: {
    name: {
      maxLength: maxLength(50),
    },
    documentation: {
      maxLength: maxLength(2048),
    },
    technology: {
      maxLength: maxLength(50),
    },
    c4type: {
      required,
    },
    landscapeElementId: {
      maxLength: maxLength(30),
    },
    extElementId: {
      maxLength: maxLength(100),
    },
    extSourceElementId: {
      maxLength: maxLength(100),
    },
    extTargetElementId: {
      maxLength: maxLength(100),
    },
    project: {
      required,
    },
    projectViews: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class ProjectElementUpdate extends Vue {
  @Inject('projectElementService') private projectElementService: () => ProjectElementService;
  @Inject('alertService') private alertService: () => AlertService;

  public projectElement: IProjectElement = new ProjectElement();

  @Inject('projectService') private projectService: () => ProjectService;

  public projects: IProject[] = [];

  @Inject('elementConfigurationService') private elementConfigurationService: () => ElementConfigurationService;

  public elementConfigurations: IElementConfiguration[] = [];

  @Inject('projectViewService') private projectViewService: () => ProjectViewService;

  public projectViews: IProjectView[] = [];
  public c4ElementTypeValues: string[] = Object.keys(C4ElementType);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.projectElementId) {
        vm.retrieveProjectElement(to.params.projectElementId);
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
    if (this.projectElement.id) {
      this.projectElementService()
        .update(this.projectElement)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('archiscapeCoreApp.projectElement.updated', { param: param.id });
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
      this.projectElementService()
        .create(this.projectElement)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('archiscapeCoreApp.projectElement.created', { param: param.id });
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

  public retrieveProjectElement(projectElementId): void {
    this.projectElementService()
      .find(projectElementId)
      .then(res => {
        this.projectElement = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.projectService()
      .retrieve()
      .then(res => {
        this.projects = res.data;
      });
    this.elementConfigurationService()
      .retrieve()
      .then(res => {
        this.elementConfigurations = res.data;
      });
    this.projectViewService()
      .retrieve()
      .then(res => {
        this.projectViews = res.data;
      });
  }
}

import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, maxLength } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import LandscapeService from '@/entities/landscape/landscape.service';
import { ILandscape } from '@/shared/model/landscape.model';

import TeamService from '@/entities/team/team.service';
import { ITeam } from '@/shared/model/team.model';

import { IProject, Project } from '@/shared/model/project.model';
import ProjectService from './project.service';

const validations: any = {
  project: {
    name: {
      required,
      maxLength: maxLength(50),
    },
    description: {},
    extProjectId: {
      maxLength: maxLength(100),
    },
    active: {
      required,
    },
    srcUrl: {
      maxLength: maxLength(2048),
    },
    contentStoreUrl: {
      maxLength: maxLength(2048),
    },
    landscape: {
      required,
    },
    team: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class ProjectUpdate extends Vue {
  @Inject('projectService') private projectService: () => ProjectService;
  @Inject('alertService') private alertService: () => AlertService;

  public project: IProject = new Project();

  @Inject('landscapeService') private landscapeService: () => LandscapeService;

  public landscapes: ILandscape[] = [];

  @Inject('teamService') private teamService: () => TeamService;

  public teams: ITeam[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.projectId) {
        vm.retrieveProject(to.params.projectId);
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
    if (this.project.id) {
      this.projectService()
        .update(this.project)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('archiscapeCoreApp.project.updated', { param: param.id });
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
      this.projectService()
        .create(this.project)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('archiscapeCoreApp.project.created', { param: param.id });
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

  public retrieveProject(projectId): void {
    this.projectService()
      .find(projectId)
      .then(res => {
        this.project = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.landscapeService()
      .retrieve()
      .then(res => {
        this.landscapes = res.data;
      });
    this.teamService()
      .retrieve()
      .then(res => {
        this.teams = res.data;
      });
  }
}

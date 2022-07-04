import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, maxLength } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import TeamService from '@/entities/team/team.service';
import { ITeam } from '@/shared/model/team.model';

import { IElementConfiguration, ElementConfiguration } from '@/shared/model/element-configuration.model';
import ElementConfigurationService from './element-configuration.service';

const validations: any = {
  elementConfiguration: {
    name: {
      required,
      maxLength: maxLength(50),
    },
    documentation: {
      required,
      maxLength: maxLength(2048),
    },
    technology: {
      required,
      maxLength: maxLength(50),
    },
    eolDate: {},
    team: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class ElementConfigurationUpdate extends Vue {
  @Inject('elementConfigurationService') private elementConfigurationService: () => ElementConfigurationService;
  @Inject('alertService') private alertService: () => AlertService;

  public elementConfiguration: IElementConfiguration = new ElementConfiguration();

  @Inject('teamService') private teamService: () => TeamService;

  public teams: ITeam[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.elementConfigurationId) {
        vm.retrieveElementConfiguration(to.params.elementConfigurationId);
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
    if (this.elementConfiguration.id) {
      this.elementConfigurationService()
        .update(this.elementConfiguration)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('archiscapeCoreApp.elementConfiguration.updated', { param: param.id });
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
      this.elementConfigurationService()
        .create(this.elementConfiguration)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('archiscapeCoreApp.elementConfiguration.created', { param: param.id });
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

  public retrieveElementConfiguration(elementConfigurationId): void {
    this.elementConfigurationService()
      .find(elementConfigurationId)
      .then(res => {
        this.elementConfiguration = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.teamService()
      .retrieve()
      .then(res => {
        this.teams = res.data;
      });
  }
}

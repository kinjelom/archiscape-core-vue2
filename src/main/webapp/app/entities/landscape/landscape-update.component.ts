import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import { ILandscape, Landscape } from '@/shared/model/landscape.model';
import LandscapeService from './landscape.service';

const validations: any = {
  landscape: {
    description: {},
  },
};

@Component({
  validations,
})
export default class LandscapeUpdate extends Vue {
  @Inject('landscapeService') private landscapeService: () => LandscapeService;
  @Inject('alertService') private alertService: () => AlertService;

  public landscape: ILandscape = new Landscape();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.landscapeId) {
        vm.retrieveLandscape(to.params.landscapeId);
      }
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
    if (this.landscape.id) {
      this.landscapeService()
        .update(this.landscape)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('archiscapeCoreApp.landscape.updated', { param: param.id });
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
      this.landscapeService()
        .create(this.landscape)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('archiscapeCoreApp.landscape.created', { param: param.id });
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

  public retrieveLandscape(landscapeId): void {
    this.landscapeService()
      .find(landscapeId)
      .then(res => {
        this.landscape = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}

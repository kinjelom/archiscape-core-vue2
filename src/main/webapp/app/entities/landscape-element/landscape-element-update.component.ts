import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, maxLength } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import LandscapeService from '@/entities/landscape/landscape.service';
import { ILandscape } from '@/shared/model/landscape.model';

import ElementConfigurationService from '@/entities/element-configuration/element-configuration.service';
import { IElementConfiguration } from '@/shared/model/element-configuration.model';

import { ILandscapeElement, LandscapeElement } from '@/shared/model/landscape-element.model';
import LandscapeElementService from './landscape-element.service';
import { C4ElementType } from '@/shared/model/enumerations/c-4-element-type.model';

const validations: any = {
  landscapeElement: {
    name: {
      required,
      maxLength: maxLength(50),
    },
    documentation: {
      required,
      maxLength: maxLength(2048),
    },
    technology: {
      maxLength: maxLength(50),
    },
    c4type: {
      required,
    },
    landscape: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class LandscapeElementUpdate extends Vue {
  @Inject('landscapeElementService') private landscapeElementService: () => LandscapeElementService;
  @Inject('alertService') private alertService: () => AlertService;

  public landscapeElement: ILandscapeElement = new LandscapeElement();

  @Inject('landscapeService') private landscapeService: () => LandscapeService;

  public landscapes: ILandscape[] = [];

  @Inject('elementConfigurationService') private elementConfigurationService: () => ElementConfigurationService;

  public elementConfigurations: IElementConfiguration[] = [];
  public c4ElementTypeValues: string[] = Object.keys(C4ElementType);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.landscapeElementId) {
        vm.retrieveLandscapeElement(to.params.landscapeElementId);
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
    if (this.landscapeElement.id) {
      this.landscapeElementService()
        .update(this.landscapeElement)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('archiscapeCoreApp.landscapeElement.updated', { param: param.id });
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
      this.landscapeElementService()
        .create(this.landscapeElement)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('archiscapeCoreApp.landscapeElement.created', { param: param.id });
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

  public retrieveLandscapeElement(landscapeElementId): void {
    this.landscapeElementService()
      .find(landscapeElementId)
      .then(res => {
        this.landscapeElement = res;
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
    this.elementConfigurationService()
      .retrieve()
      .then(res => {
        this.elementConfigurations = res.data;
      });
  }
}

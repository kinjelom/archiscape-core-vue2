import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { numeric, required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import { ITextFileContent, TextFileContent } from '@/shared/model/text-file-content.model';
import TextFileContentService from './text-file-content.service';

const validations: any = {
  textFileContent: {
    version: {
      required,
      numeric,
    },
    insertDate: {
      required,
    },
    fileName: {},
    content: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class TextFileContentUpdate extends mixins(JhiDataUtils) {
  @Inject('textFileContentService') private textFileContentService: () => TextFileContentService;
  @Inject('alertService') private alertService: () => AlertService;

  public textFileContent: ITextFileContent = new TextFileContent();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.textFileContentId) {
        vm.retrieveTextFileContent(to.params.textFileContentId);
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
    if (this.textFileContent.id) {
      this.textFileContentService()
        .update(this.textFileContent)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('archiscapeCoreApp.textFileContent.updated', { param: param.id });
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
      this.textFileContentService()
        .create(this.textFileContent)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('archiscapeCoreApp.textFileContent.created', { param: param.id });
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

  public retrieveTextFileContent(textFileContentId): void {
    this.textFileContentService()
      .find(textFileContentId)
      .then(res => {
        this.textFileContent = res;
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

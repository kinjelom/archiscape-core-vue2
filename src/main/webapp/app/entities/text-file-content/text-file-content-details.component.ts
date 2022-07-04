import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { ITextFileContent } from '@/shared/model/text-file-content.model';
import TextFileContentService from './text-file-content.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class TextFileContentDetails extends mixins(JhiDataUtils) {
  @Inject('textFileContentService') private textFileContentService: () => TextFileContentService;
  @Inject('alertService') private alertService: () => AlertService;

  public textFileContent: ITextFileContent = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.textFileContentId) {
        vm.retrieveTextFileContent(to.params.textFileContentId);
      }
    });
  }

  public retrieveTextFileContent(textFileContentId) {
    this.textFileContentService()
      .find(textFileContentId)
      .then(res => {
        this.textFileContent = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}

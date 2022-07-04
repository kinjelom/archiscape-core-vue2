import { Component, Vue, Inject } from 'vue-property-decorator';

import { IElementConfiguration } from '@/shared/model/element-configuration.model';
import ElementConfigurationService from './element-configuration.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ElementConfigurationDetails extends Vue {
  @Inject('elementConfigurationService') private elementConfigurationService: () => ElementConfigurationService;
  @Inject('alertService') private alertService: () => AlertService;

  public elementConfiguration: IElementConfiguration = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.elementConfigurationId) {
        vm.retrieveElementConfiguration(to.params.elementConfigurationId);
      }
    });
  }

  public retrieveElementConfiguration(elementConfigurationId) {
    this.elementConfigurationService()
      .find(elementConfigurationId)
      .then(res => {
        this.elementConfiguration = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}

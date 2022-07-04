import { Component, Vue, Inject } from 'vue-property-decorator';

import { ILandscapeElement } from '@/shared/model/landscape-element.model';
import LandscapeElementService from './landscape-element.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class LandscapeElementDetails extends Vue {
  @Inject('landscapeElementService') private landscapeElementService: () => LandscapeElementService;
  @Inject('alertService') private alertService: () => AlertService;

  public landscapeElement: ILandscapeElement = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.landscapeElementId) {
        vm.retrieveLandscapeElement(to.params.landscapeElementId);
      }
    });
  }

  public retrieveLandscapeElement(landscapeElementId) {
    this.landscapeElementService()
      .find(landscapeElementId)
      .then(res => {
        this.landscapeElement = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}

import { Component, Vue, Inject } from 'vue-property-decorator';

import { ILandscape } from '@/shared/model/landscape.model';
import LandscapeService from './landscape.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class LandscapeDetails extends Vue {
  @Inject('landscapeService') private landscapeService: () => LandscapeService;
  @Inject('alertService') private alertService: () => AlertService;

  public landscape: ILandscape = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.landscapeId) {
        vm.retrieveLandscape(to.params.landscapeId);
      }
    });
  }

  public retrieveLandscape(landscapeId) {
    this.landscapeService()
      .find(landscapeId)
      .then(res => {
        this.landscape = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}

import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { IProjectView } from '@/shared/model/project-view.model';
import ProjectViewService from './project-view.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ProjectViewDetails extends mixins(JhiDataUtils) {
  @Inject('projectViewService') private projectViewService: () => ProjectViewService;
  @Inject('alertService') private alertService: () => AlertService;

  public projectView: IProjectView = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.projectViewId) {
        vm.retrieveProjectView(to.params.projectViewId);
      }
    });
  }

  public retrieveProjectView(projectViewId) {
    this.projectViewService()
      .find(projectViewId)
      .then(res => {
        this.projectView = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}

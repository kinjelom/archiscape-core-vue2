import { Component, Vue, Inject } from 'vue-property-decorator';

import { IProjectElement } from '@/shared/model/project-element.model';
import ProjectElementService from './project-element.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ProjectElementDetails extends Vue {
  @Inject('projectElementService') private projectElementService: () => ProjectElementService;
  @Inject('alertService') private alertService: () => AlertService;

  public projectElement: IProjectElement = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.projectElementId) {
        vm.retrieveProjectElement(to.params.projectElementId);
      }
    });
  }

  public retrieveProjectElement(projectElementId) {
    this.projectElementService()
      .find(projectElementId)
      .then(res => {
        this.projectElement = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}

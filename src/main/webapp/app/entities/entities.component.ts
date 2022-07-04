import { Component, Provide, Vue } from 'vue-property-decorator';

import UserService from '@/entities/user/user.service';
import TeamService from './team/team.service';
import LandscapeService from './landscape/landscape.service';
import ElementConfigurationService from './element-configuration/element-configuration.service';
import LandscapeElementService from './landscape-element/landscape-element.service';
import ProjectService from './project/project.service';
import ProjectElementService from './project-element/project-element.service';
import ProjectViewService from './project-view/project-view.service';
import TextFileContentService from './text-file-content/text-file-content.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

@Component
export default class Entities extends Vue {
  @Provide('userService') private userService = () => new UserService();
  @Provide('teamService') private teamService = () => new TeamService();
  @Provide('landscapeService') private landscapeService = () => new LandscapeService();
  @Provide('elementConfigurationService') private elementConfigurationService = () => new ElementConfigurationService();
  @Provide('landscapeElementService') private landscapeElementService = () => new LandscapeElementService();
  @Provide('projectService') private projectService = () => new ProjectService();
  @Provide('projectElementService') private projectElementService = () => new ProjectElementService();
  @Provide('projectViewService') private projectViewService = () => new ProjectViewService();
  @Provide('textFileContentService') private textFileContentService = () => new TextFileContentService();
  // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
}

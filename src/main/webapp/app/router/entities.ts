import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

// prettier-ignore
const Team = () => import('@/entities/team/team.vue');
// prettier-ignore
const TeamUpdate = () => import('@/entities/team/team-update.vue');
// prettier-ignore
const TeamDetails = () => import('@/entities/team/team-details.vue');
// prettier-ignore
const Landscape = () => import('@/entities/landscape/landscape.vue');
// prettier-ignore
const LandscapeUpdate = () => import('@/entities/landscape/landscape-update.vue');
// prettier-ignore
const LandscapeDetails = () => import('@/entities/landscape/landscape-details.vue');
// prettier-ignore
const ElementConfiguration = () => import('@/entities/element-configuration/element-configuration.vue');
// prettier-ignore
const ElementConfigurationUpdate = () => import('@/entities/element-configuration/element-configuration-update.vue');
// prettier-ignore
const ElementConfigurationDetails = () => import('@/entities/element-configuration/element-configuration-details.vue');
// prettier-ignore
const LandscapeElement = () => import('@/entities/landscape-element/landscape-element.vue');
// prettier-ignore
const LandscapeElementUpdate = () => import('@/entities/landscape-element/landscape-element-update.vue');
// prettier-ignore
const LandscapeElementDetails = () => import('@/entities/landscape-element/landscape-element-details.vue');
// prettier-ignore
const Project = () => import('@/entities/project/project.vue');
// prettier-ignore
const ProjectUpdate = () => import('@/entities/project/project-update.vue');
// prettier-ignore
const ProjectDetails = () => import('@/entities/project/project-details.vue');
// prettier-ignore
const ProjectElement = () => import('@/entities/project-element/project-element.vue');
// prettier-ignore
const ProjectElementUpdate = () => import('@/entities/project-element/project-element-update.vue');
// prettier-ignore
const ProjectElementDetails = () => import('@/entities/project-element/project-element-details.vue');
// prettier-ignore
const ProjectView = () => import('@/entities/project-view/project-view.vue');
// prettier-ignore
const ProjectViewUpdate = () => import('@/entities/project-view/project-view-update.vue');
// prettier-ignore
const ProjectViewDetails = () => import('@/entities/project-view/project-view-details.vue');
// prettier-ignore
const TextFileContent = () => import('@/entities/text-file-content/text-file-content.vue');
// prettier-ignore
const TextFileContentUpdate = () => import('@/entities/text-file-content/text-file-content-update.vue');
// prettier-ignore
const TextFileContentDetails = () => import('@/entities/text-file-content/text-file-content-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'team',
      name: 'Team',
      component: Team,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'team/new',
      name: 'TeamCreate',
      component: TeamUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'team/:teamId/edit',
      name: 'TeamEdit',
      component: TeamUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'team/:teamId/view',
      name: 'TeamView',
      component: TeamDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'landscape',
      name: 'Landscape',
      component: Landscape,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'landscape/new',
      name: 'LandscapeCreate',
      component: LandscapeUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'landscape/:landscapeId/edit',
      name: 'LandscapeEdit',
      component: LandscapeUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'landscape/:landscapeId/view',
      name: 'LandscapeView',
      component: LandscapeDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'element-configuration',
      name: 'ElementConfiguration',
      component: ElementConfiguration,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'element-configuration/new',
      name: 'ElementConfigurationCreate',
      component: ElementConfigurationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'element-configuration/:elementConfigurationId/edit',
      name: 'ElementConfigurationEdit',
      component: ElementConfigurationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'element-configuration/:elementConfigurationId/view',
      name: 'ElementConfigurationView',
      component: ElementConfigurationDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'landscape-element',
      name: 'LandscapeElement',
      component: LandscapeElement,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'landscape-element/new',
      name: 'LandscapeElementCreate',
      component: LandscapeElementUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'landscape-element/:landscapeElementId/edit',
      name: 'LandscapeElementEdit',
      component: LandscapeElementUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'landscape-element/:landscapeElementId/view',
      name: 'LandscapeElementView',
      component: LandscapeElementDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'project',
      name: 'Project',
      component: Project,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'project/new',
      name: 'ProjectCreate',
      component: ProjectUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'project/:projectId/edit',
      name: 'ProjectEdit',
      component: ProjectUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'project/:projectId/view',
      name: 'ProjectView',
      component: ProjectDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'project-element',
      name: 'ProjectElement',
      component: ProjectElement,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'project-element/new',
      name: 'ProjectElementCreate',
      component: ProjectElementUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'project-element/:projectElementId/edit',
      name: 'ProjectElementEdit',
      component: ProjectElementUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'project-element/:projectElementId/view',
      name: 'ProjectElementView',
      component: ProjectElementDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'project-view',
      name: 'ProjectView',
      component: ProjectView,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'project-view/new',
      name: 'ProjectViewCreate',
      component: ProjectViewUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'project-view/:projectViewId/edit',
      name: 'ProjectViewEdit',
      component: ProjectViewUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'project-view/:projectViewId/view',
      name: 'ProjectViewView',
      component: ProjectViewDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'text-file-content',
      name: 'TextFileContent',
      component: TextFileContent,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'text-file-content/new',
      name: 'TextFileContentCreate',
      component: TextFileContentUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'text-file-content/:textFileContentId/edit',
      name: 'TextFileContentEdit',
      component: TextFileContentUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'text-file-content/:textFileContentId/view',
      name: 'TextFileContentView',
      component: TextFileContentDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};

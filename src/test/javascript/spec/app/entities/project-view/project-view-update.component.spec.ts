/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ProjectViewUpdateComponent from '@/entities/project-view/project-view-update.vue';
import ProjectViewClass from '@/entities/project-view/project-view-update.component';
import ProjectViewService from '@/entities/project-view/project-view.service';

import ProjectService from '@/entities/project/project.service';

import ProjectElementService from '@/entities/project-element/project-element.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('ProjectView Management Update Component', () => {
    let wrapper: Wrapper<ProjectViewClass>;
    let comp: ProjectViewClass;
    let projectViewServiceStub: SinonStubbedInstance<ProjectViewService>;

    beforeEach(() => {
      projectViewServiceStub = sinon.createStubInstance<ProjectViewService>(ProjectViewService);

      wrapper = shallowMount<ProjectViewClass>(ProjectViewUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          projectViewService: () => projectViewServiceStub,
          alertService: () => new AlertService(),

          projectService: () =>
            sinon.createStubInstance<ProjectService>(ProjectService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          projectElementService: () =>
            sinon.createStubInstance<ProjectElementService>(ProjectElementService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.projectView = entity;
        projectViewServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(projectViewServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.projectView = entity;
        projectViewServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(projectViewServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundProjectView = { id: 123 };
        projectViewServiceStub.find.resolves(foundProjectView);
        projectViewServiceStub.retrieve.resolves([foundProjectView]);

        // WHEN
        comp.beforeRouteEnter({ params: { projectViewId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.projectView).toBe(foundProjectView);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});

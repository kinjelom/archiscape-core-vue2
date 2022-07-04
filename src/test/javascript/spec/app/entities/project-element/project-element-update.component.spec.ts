/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ProjectElementUpdateComponent from '@/entities/project-element/project-element-update.vue';
import ProjectElementClass from '@/entities/project-element/project-element-update.component';
import ProjectElementService from '@/entities/project-element/project-element.service';

import ProjectService from '@/entities/project/project.service';

import ElementConfigurationService from '@/entities/element-configuration/element-configuration.service';

import ProjectViewService from '@/entities/project-view/project-view.service';
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
  describe('ProjectElement Management Update Component', () => {
    let wrapper: Wrapper<ProjectElementClass>;
    let comp: ProjectElementClass;
    let projectElementServiceStub: SinonStubbedInstance<ProjectElementService>;

    beforeEach(() => {
      projectElementServiceStub = sinon.createStubInstance<ProjectElementService>(ProjectElementService);

      wrapper = shallowMount<ProjectElementClass>(ProjectElementUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          projectElementService: () => projectElementServiceStub,
          alertService: () => new AlertService(),

          projectService: () =>
            sinon.createStubInstance<ProjectService>(ProjectService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          elementConfigurationService: () =>
            sinon.createStubInstance<ElementConfigurationService>(ElementConfigurationService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          projectViewService: () =>
            sinon.createStubInstance<ProjectViewService>(ProjectViewService, {
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
        comp.projectElement = entity;
        projectElementServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(projectElementServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.projectElement = entity;
        projectElementServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(projectElementServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundProjectElement = { id: 123 };
        projectElementServiceStub.find.resolves(foundProjectElement);
        projectElementServiceStub.retrieve.resolves([foundProjectElement]);

        // WHEN
        comp.beforeRouteEnter({ params: { projectElementId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.projectElement).toBe(foundProjectElement);
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

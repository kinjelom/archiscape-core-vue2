/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ProjectViewDetailComponent from '@/entities/project-view/project-view-details.vue';
import ProjectViewClass from '@/entities/project-view/project-view-details.component';
import ProjectViewService from '@/entities/project-view/project-view.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('ProjectView Management Detail Component', () => {
    let wrapper: Wrapper<ProjectViewClass>;
    let comp: ProjectViewClass;
    let projectViewServiceStub: SinonStubbedInstance<ProjectViewService>;

    beforeEach(() => {
      projectViewServiceStub = sinon.createStubInstance<ProjectViewService>(ProjectViewService);

      wrapper = shallowMount<ProjectViewClass>(ProjectViewDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { projectViewService: () => projectViewServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundProjectView = { id: 123 };
        projectViewServiceStub.find.resolves(foundProjectView);

        // WHEN
        comp.retrieveProjectView(123);
        await comp.$nextTick();

        // THEN
        expect(comp.projectView).toBe(foundProjectView);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundProjectView = { id: 123 };
        projectViewServiceStub.find.resolves(foundProjectView);

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

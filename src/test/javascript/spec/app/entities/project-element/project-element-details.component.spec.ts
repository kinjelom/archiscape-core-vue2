/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ProjectElementDetailComponent from '@/entities/project-element/project-element-details.vue';
import ProjectElementClass from '@/entities/project-element/project-element-details.component';
import ProjectElementService from '@/entities/project-element/project-element.service';
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
  describe('ProjectElement Management Detail Component', () => {
    let wrapper: Wrapper<ProjectElementClass>;
    let comp: ProjectElementClass;
    let projectElementServiceStub: SinonStubbedInstance<ProjectElementService>;

    beforeEach(() => {
      projectElementServiceStub = sinon.createStubInstance<ProjectElementService>(ProjectElementService);

      wrapper = shallowMount<ProjectElementClass>(ProjectElementDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { projectElementService: () => projectElementServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundProjectElement = { id: 123 };
        projectElementServiceStub.find.resolves(foundProjectElement);

        // WHEN
        comp.retrieveProjectElement(123);
        await comp.$nextTick();

        // THEN
        expect(comp.projectElement).toBe(foundProjectElement);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundProjectElement = { id: 123 };
        projectElementServiceStub.find.resolves(foundProjectElement);

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

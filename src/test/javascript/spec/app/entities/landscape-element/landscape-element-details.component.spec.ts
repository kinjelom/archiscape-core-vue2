/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import LandscapeElementDetailComponent from '@/entities/landscape-element/landscape-element-details.vue';
import LandscapeElementClass from '@/entities/landscape-element/landscape-element-details.component';
import LandscapeElementService from '@/entities/landscape-element/landscape-element.service';
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
  describe('LandscapeElement Management Detail Component', () => {
    let wrapper: Wrapper<LandscapeElementClass>;
    let comp: LandscapeElementClass;
    let landscapeElementServiceStub: SinonStubbedInstance<LandscapeElementService>;

    beforeEach(() => {
      landscapeElementServiceStub = sinon.createStubInstance<LandscapeElementService>(LandscapeElementService);

      wrapper = shallowMount<LandscapeElementClass>(LandscapeElementDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { landscapeElementService: () => landscapeElementServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundLandscapeElement = { id: 'ABC' };
        landscapeElementServiceStub.find.resolves(foundLandscapeElement);

        // WHEN
        comp.retrieveLandscapeElement('ABC');
        await comp.$nextTick();

        // THEN
        expect(comp.landscapeElement).toBe(foundLandscapeElement);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundLandscapeElement = { id: 'ABC' };
        landscapeElementServiceStub.find.resolves(foundLandscapeElement);

        // WHEN
        comp.beforeRouteEnter({ params: { landscapeElementId: 'ABC' } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.landscapeElement).toBe(foundLandscapeElement);
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

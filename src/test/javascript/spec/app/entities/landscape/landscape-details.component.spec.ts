/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import LandscapeDetailComponent from '@/entities/landscape/landscape-details.vue';
import LandscapeClass from '@/entities/landscape/landscape-details.component';
import LandscapeService from '@/entities/landscape/landscape.service';
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
  describe('Landscape Management Detail Component', () => {
    let wrapper: Wrapper<LandscapeClass>;
    let comp: LandscapeClass;
    let landscapeServiceStub: SinonStubbedInstance<LandscapeService>;

    beforeEach(() => {
      landscapeServiceStub = sinon.createStubInstance<LandscapeService>(LandscapeService);

      wrapper = shallowMount<LandscapeClass>(LandscapeDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { landscapeService: () => landscapeServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundLandscape = { id: 'ABC' };
        landscapeServiceStub.find.resolves(foundLandscape);

        // WHEN
        comp.retrieveLandscape('ABC');
        await comp.$nextTick();

        // THEN
        expect(comp.landscape).toBe(foundLandscape);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundLandscape = { id: 'ABC' };
        landscapeServiceStub.find.resolves(foundLandscape);

        // WHEN
        comp.beforeRouteEnter({ params: { landscapeId: 'ABC' } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.landscape).toBe(foundLandscape);
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

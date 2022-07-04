/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import LandscapeUpdateComponent from '@/entities/landscape/landscape-update.vue';
import LandscapeClass from '@/entities/landscape/landscape-update.component';
import LandscapeService from '@/entities/landscape/landscape.service';

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
  describe('Landscape Management Update Component', () => {
    let wrapper: Wrapper<LandscapeClass>;
    let comp: LandscapeClass;
    let landscapeServiceStub: SinonStubbedInstance<LandscapeService>;

    beforeEach(() => {
      landscapeServiceStub = sinon.createStubInstance<LandscapeService>(LandscapeService);

      wrapper = shallowMount<LandscapeClass>(LandscapeUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          landscapeService: () => landscapeServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 'ABC' };
        comp.landscape = entity;
        landscapeServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(landscapeServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.landscape = entity;
        landscapeServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(landscapeServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundLandscape = { id: 'ABC' };
        landscapeServiceStub.find.resolves(foundLandscape);
        landscapeServiceStub.retrieve.resolves([foundLandscape]);

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

/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import LandscapeElementUpdateComponent from '@/entities/landscape-element/landscape-element-update.vue';
import LandscapeElementClass from '@/entities/landscape-element/landscape-element-update.component';
import LandscapeElementService from '@/entities/landscape-element/landscape-element.service';

import LandscapeService from '@/entities/landscape/landscape.service';

import ElementConfigurationService from '@/entities/element-configuration/element-configuration.service';
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
  describe('LandscapeElement Management Update Component', () => {
    let wrapper: Wrapper<LandscapeElementClass>;
    let comp: LandscapeElementClass;
    let landscapeElementServiceStub: SinonStubbedInstance<LandscapeElementService>;

    beforeEach(() => {
      landscapeElementServiceStub = sinon.createStubInstance<LandscapeElementService>(LandscapeElementService);

      wrapper = shallowMount<LandscapeElementClass>(LandscapeElementUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          landscapeElementService: () => landscapeElementServiceStub,
          alertService: () => new AlertService(),

          landscapeService: () =>
            sinon.createStubInstance<LandscapeService>(LandscapeService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          elementConfigurationService: () =>
            sinon.createStubInstance<ElementConfigurationService>(ElementConfigurationService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 'ABC' };
        comp.landscapeElement = entity;
        landscapeElementServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(landscapeElementServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.landscapeElement = entity;
        landscapeElementServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(landscapeElementServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundLandscapeElement = { id: 'ABC' };
        landscapeElementServiceStub.find.resolves(foundLandscapeElement);
        landscapeElementServiceStub.retrieve.resolves([foundLandscapeElement]);

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

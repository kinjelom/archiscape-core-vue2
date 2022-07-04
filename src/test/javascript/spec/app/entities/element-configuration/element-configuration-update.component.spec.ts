/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ElementConfigurationUpdateComponent from '@/entities/element-configuration/element-configuration-update.vue';
import ElementConfigurationClass from '@/entities/element-configuration/element-configuration-update.component';
import ElementConfigurationService from '@/entities/element-configuration/element-configuration.service';

import TeamService from '@/entities/team/team.service';
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
  describe('ElementConfiguration Management Update Component', () => {
    let wrapper: Wrapper<ElementConfigurationClass>;
    let comp: ElementConfigurationClass;
    let elementConfigurationServiceStub: SinonStubbedInstance<ElementConfigurationService>;

    beforeEach(() => {
      elementConfigurationServiceStub = sinon.createStubInstance<ElementConfigurationService>(ElementConfigurationService);

      wrapper = shallowMount<ElementConfigurationClass>(ElementConfigurationUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          elementConfigurationService: () => elementConfigurationServiceStub,
          alertService: () => new AlertService(),

          teamService: () =>
            sinon.createStubInstance<TeamService>(TeamService, {
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
        comp.elementConfiguration = entity;
        elementConfigurationServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(elementConfigurationServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.elementConfiguration = entity;
        elementConfigurationServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(elementConfigurationServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundElementConfiguration = { id: 'ABC' };
        elementConfigurationServiceStub.find.resolves(foundElementConfiguration);
        elementConfigurationServiceStub.retrieve.resolves([foundElementConfiguration]);

        // WHEN
        comp.beforeRouteEnter({ params: { elementConfigurationId: 'ABC' } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.elementConfiguration).toBe(foundElementConfiguration);
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

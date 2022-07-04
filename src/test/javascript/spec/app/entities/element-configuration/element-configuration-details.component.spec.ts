/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ElementConfigurationDetailComponent from '@/entities/element-configuration/element-configuration-details.vue';
import ElementConfigurationClass from '@/entities/element-configuration/element-configuration-details.component';
import ElementConfigurationService from '@/entities/element-configuration/element-configuration.service';
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
  describe('ElementConfiguration Management Detail Component', () => {
    let wrapper: Wrapper<ElementConfigurationClass>;
    let comp: ElementConfigurationClass;
    let elementConfigurationServiceStub: SinonStubbedInstance<ElementConfigurationService>;

    beforeEach(() => {
      elementConfigurationServiceStub = sinon.createStubInstance<ElementConfigurationService>(ElementConfigurationService);

      wrapper = shallowMount<ElementConfigurationClass>(ElementConfigurationDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { elementConfigurationService: () => elementConfigurationServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundElementConfiguration = { id: 'ABC' };
        elementConfigurationServiceStub.find.resolves(foundElementConfiguration);

        // WHEN
        comp.retrieveElementConfiguration('ABC');
        await comp.$nextTick();

        // THEN
        expect(comp.elementConfiguration).toBe(foundElementConfiguration);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundElementConfiguration = { id: 'ABC' };
        elementConfigurationServiceStub.find.resolves(foundElementConfiguration);

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

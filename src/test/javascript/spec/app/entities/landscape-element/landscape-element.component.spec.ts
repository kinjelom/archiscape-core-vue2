/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import LandscapeElementComponent from '@/entities/landscape-element/landscape-element.vue';
import LandscapeElementClass from '@/entities/landscape-element/landscape-element.component';
import LandscapeElementService from '@/entities/landscape-element/landscape-element.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(ToastPlugin);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.component('jhi-sort-indicator', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('LandscapeElement Management Component', () => {
    let wrapper: Wrapper<LandscapeElementClass>;
    let comp: LandscapeElementClass;
    let landscapeElementServiceStub: SinonStubbedInstance<LandscapeElementService>;

    beforeEach(() => {
      landscapeElementServiceStub = sinon.createStubInstance<LandscapeElementService>(LandscapeElementService);
      landscapeElementServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<LandscapeElementClass>(LandscapeElementComponent, {
        store,
        i18n,
        localVue,
        stubs: { jhiItemCount: true, bPagination: true, bModal: bModalStub as any },
        provide: {
          landscapeElementService: () => landscapeElementServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      landscapeElementServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 'ABC' }] });

      // WHEN
      comp.retrieveAllLandscapeElements();
      await comp.$nextTick();

      // THEN
      expect(landscapeElementServiceStub.retrieve.called).toBeTruthy();
      expect(comp.landscapeElements[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
    });

    it('should load a page', async () => {
      // GIVEN
      landscapeElementServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 'ABC' }] });
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();

      // THEN
      expect(landscapeElementServiceStub.retrieve.called).toBeTruthy();
      expect(comp.landscapeElements[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
    });

    it('should not load a page if the page is the same as the previous page', () => {
      // GIVEN
      landscapeElementServiceStub.retrieve.reset();
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(landscapeElementServiceStub.retrieve.called).toBeFalsy();
    });

    it('should re-initialize the page', async () => {
      // GIVEN
      landscapeElementServiceStub.retrieve.reset();
      landscapeElementServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 'ABC' }] });

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();
      comp.clear();
      await comp.$nextTick();

      // THEN
      expect(landscapeElementServiceStub.retrieve.callCount).toEqual(3);
      expect(comp.page).toEqual(1);
      expect(comp.landscapeElements[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,asc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // GIVEN
      comp.propOrder = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,asc', 'id']);
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      landscapeElementServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 'ABC' });
      expect(landscapeElementServiceStub.retrieve.callCount).toEqual(1);

      comp.removeLandscapeElement();
      await comp.$nextTick();

      // THEN
      expect(landscapeElementServiceStub.delete.called).toBeTruthy();
      expect(landscapeElementServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});

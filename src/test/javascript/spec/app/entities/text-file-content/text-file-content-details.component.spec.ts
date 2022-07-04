/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import TextFileContentDetailComponent from '@/entities/text-file-content/text-file-content-details.vue';
import TextFileContentClass from '@/entities/text-file-content/text-file-content-details.component';
import TextFileContentService from '@/entities/text-file-content/text-file-content.service';
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
  describe('TextFileContent Management Detail Component', () => {
    let wrapper: Wrapper<TextFileContentClass>;
    let comp: TextFileContentClass;
    let textFileContentServiceStub: SinonStubbedInstance<TextFileContentService>;

    beforeEach(() => {
      textFileContentServiceStub = sinon.createStubInstance<TextFileContentService>(TextFileContentService);

      wrapper = shallowMount<TextFileContentClass>(TextFileContentDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { textFileContentService: () => textFileContentServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundTextFileContent = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        textFileContentServiceStub.find.resolves(foundTextFileContent);

        // WHEN
        comp.retrieveTextFileContent('9fec3727-3421-4967-b213-ba36557ca194');
        await comp.$nextTick();

        // THEN
        expect(comp.textFileContent).toBe(foundTextFileContent);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTextFileContent = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        textFileContentServiceStub.find.resolves(foundTextFileContent);

        // WHEN
        comp.beforeRouteEnter({ params: { textFileContentId: '9fec3727-3421-4967-b213-ba36557ca194' } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.textFileContent).toBe(foundTextFileContent);
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

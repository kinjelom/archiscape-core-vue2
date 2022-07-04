/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import { DATE_FORMAT } from '@/shared/date/filters';
import TextFileContentService from '@/entities/text-file-content/text-file-content.service';
import { TextFileContent } from '@/shared/model/text-file-content.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('TextFileContent Service', () => {
    let service: TextFileContentService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new TextFileContentService();
      currentDate = new Date();
      elemDefault = new TextFileContent('9fec3727-3421-4967-b213-ba36557ca194', 0, currentDate, 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            insertDate: dayjs(currentDate).format(DATE_FORMAT),
          },
          elemDefault
        );
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find('9fec3727-3421-4967-b213-ba36557ca194').then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find('9fec3727-3421-4967-b213-ba36557ca194')
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a TextFileContent', async () => {
        const returnedFromService = Object.assign(
          {
            id: '9fec3727-3421-4967-b213-ba36557ca194',
            insertDate: dayjs(currentDate).format(DATE_FORMAT),
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            insertDate: currentDate,
          },
          returnedFromService
        );

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a TextFileContent', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a TextFileContent', async () => {
        const returnedFromService = Object.assign(
          {
            version: 1,
            insertDate: dayjs(currentDate).format(DATE_FORMAT),
            fileName: 'BBBBBB',
            content: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            insertDate: currentDate,
          },
          returnedFromService
        );
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a TextFileContent', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a TextFileContent', async () => {
        const patchObject = Object.assign(
          {
            version: 1,
          },
          new TextFileContent()
        );
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            insertDate: currentDate,
          },
          returnedFromService
        );
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a TextFileContent', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of TextFileContent', async () => {
        const returnedFromService = Object.assign(
          {
            version: 1,
            insertDate: dayjs(currentDate).format(DATE_FORMAT),
            fileName: 'BBBBBB',
            content: 'BBBBBB',
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            insertDate: currentDate,
          },
          returnedFromService
        );
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of TextFileContent', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a TextFileContent', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete('9fec3727-3421-4967-b213-ba36557ca194').then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a TextFileContent', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete('9fec3727-3421-4967-b213-ba36557ca194')
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});

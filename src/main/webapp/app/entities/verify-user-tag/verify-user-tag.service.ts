import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { DateUtils } from 'ng-jhipster';

import { VerifyUserTag } from './verify-user-tag.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class VerifyUserTagService {

    private resourceUrl = 'api/verify-user-tags';

    constructor(private http: Http, private dateUtils: DateUtils) { }

    create(verifyUserTag: VerifyUserTag): Observable<VerifyUserTag> {
        const copy = this.convert(verifyUserTag);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(verifyUserTag: VerifyUserTag): Observable<VerifyUserTag> {
        const copy = this.convert(verifyUserTag);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<VerifyUserTag> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.verifyDate = this.dateUtils
            .convertLocalDateFromServer(entity.verifyDate);
    }

    private convert(verifyUserTag: VerifyUserTag): VerifyUserTag {
        const copy: VerifyUserTag = Object.assign({}, verifyUserTag);
        copy.verifyDate = this.dateUtils
            .convertLocalDateToServer(verifyUserTag.verifyDate);
        return copy;
    }
}

import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { DateUtils } from 'ng-jhipster';

import { TagRequest } from './tag-request.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TagRequestService {

    private resourceUrl = 'api/tag-requests';
    private resourceFileUrl = 'api/tag-requests-file';

    constructor(private http: Http, private dateUtils: DateUtils) { }

    create(tagRequest: TagRequest): Observable<TagRequest> {
        const copy = this.convert(tagRequest);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(tagRequest: TagRequest): Observable<TagRequest> {
        const copy = this.convert(tagRequest);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<TagRequest> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }
    
    findFile(id: number): Observable<TagRequest> {
        return this.http.get(`${this.resourceFileUrl}/${id}`).map((res: Response) => {
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
        entity.createDate = this.dateUtils
            .convertLocalDateFromServer(entity.createDate);
    }

    private convert(tagRequest: TagRequest): TagRequest {
        const copy: TagRequest = Object.assign({}, tagRequest);
        copy.createDate = this.dateUtils
            .convertLocalDateToServer(tagRequest.createDate);
        return copy;
    }
}

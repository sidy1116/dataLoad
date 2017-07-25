import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { DateUtils } from 'ng-jhipster';

import { RadsInventory } from './rads-inventory.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class RadsInventoryService {

    private resourceUrl = 'api/rads-inventories';

    constructor(private http: Http, private dateUtils: DateUtils) { }

    create(radsInventory: RadsInventory): Observable<RadsInventory> {
        const copy = this.convert(radsInventory);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(radsInventory: RadsInventory): Observable<RadsInventory> {
        const copy = this.convert(radsInventory);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<RadsInventory> {
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
        entity.inventoryDate = this.dateUtils
            .convertLocalDateFromServer(entity.inventoryDate);
    }

    private convert(radsInventory: RadsInventory): RadsInventory {
        const copy: RadsInventory = Object.assign({}, radsInventory);
        copy.inventoryDate = this.dateUtils
            .convertLocalDateToServer(radsInventory.inventoryDate);
        return copy;
    }
}

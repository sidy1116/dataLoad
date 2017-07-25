import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { RadsCategoryInventory } from './rads-category-inventory.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class RadsCategoryInventoryService {

    private resourceUrl = 'api/rads-category-inventories';

    constructor(private http: Http) { }

    create(radsCategoryInventory: RadsCategoryInventory): Observable<RadsCategoryInventory> {
        const copy = this.convert(radsCategoryInventory);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(radsCategoryInventory: RadsCategoryInventory): Observable<RadsCategoryInventory> {
        const copy = this.convert(radsCategoryInventory);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<RadsCategoryInventory> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
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
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(radsCategoryInventory: RadsCategoryInventory): RadsCategoryInventory {
        const copy: RadsCategoryInventory = Object.assign({}, radsCategoryInventory);
        return copy;
    }
}

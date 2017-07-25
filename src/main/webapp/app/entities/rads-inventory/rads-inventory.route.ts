import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { RadsInventoryComponent } from './rads-inventory.component';
import { RadsInventoryDetailComponent } from './rads-inventory-detail.component';
import { RadsInventoryPopupComponent } from './rads-inventory-dialog.component';
import { RadsInventoryDeletePopupComponent } from './rads-inventory-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class RadsInventoryResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: PaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const radsInventoryRoute: Routes = [
    {
        path: 'rads-inventory',
        component: RadsInventoryComponent,
        resolve: {
            'pagingParams': RadsInventoryResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RadsInventories'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'rads-inventory/:id',
        component: RadsInventoryDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RadsInventories'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const radsInventoryPopupRoute: Routes = [
    {
        path: 'rads-inventory-new',
        component: RadsInventoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RadsInventories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'rads-inventory/:id/edit',
        component: RadsInventoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RadsInventories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'rads-inventory/:id/delete',
        component: RadsInventoryDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RadsInventories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { RadsCategoryInventoryComponent } from './rads-category-inventory.component';
import { RadsCategoryInventoryDetailComponent } from './rads-category-inventory-detail.component';
import { RadsCategoryInventoryPopupComponent } from './rads-category-inventory-dialog.component';
import { RadsCategoryInventoryDeletePopupComponent } from './rads-category-inventory-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class RadsCategoryInventoryResolvePagingParams implements Resolve<any> {

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

export const radsCategoryInventoryRoute: Routes = [
    {
        path: 'rads-category-inventory',
        component: RadsCategoryInventoryComponent,
        resolve: {
            'pagingParams': RadsCategoryInventoryResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RadsCategoryInventories'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'rads-category-inventory/:id',
        component: RadsCategoryInventoryDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RadsCategoryInventories'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const radsCategoryInventoryPopupRoute: Routes = [
    {
        path: 'rads-category-inventory-new',
        component: RadsCategoryInventoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RadsCategoryInventories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'rads-category-inventory/:id/edit',
        component: RadsCategoryInventoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RadsCategoryInventories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'rads-category-inventory/:id/delete',
        component: RadsCategoryInventoryDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RadsCategoryInventories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

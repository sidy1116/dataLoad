import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { VerifyUserTagComponent } from './verify-user-tag.component';
import { VerifyUserTagDetailComponent } from './verify-user-tag-detail.component';
import { VerifyUserTagPopupComponent } from './verify-user-tag-dialog.component';
import { VerifyUserTagDeletePopupComponent } from './verify-user-tag-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class VerifyUserTagResolvePagingParams implements Resolve<any> {

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

export const verifyUserTagRoute: Routes = [
    {
        path: 'verify-user-tag',
        component: VerifyUserTagComponent,
        resolve: {
            'pagingParams': VerifyUserTagResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'VerifyUserTags'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'verify-user-tag/:id',
        component: VerifyUserTagDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'VerifyUserTags'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const verifyUserTagPopupRoute: Routes = [
    {
        path: 'verify-user-tag-new',
        component: VerifyUserTagPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'VerifyUserTags'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'verify-user-tag/:id/edit',
        component: VerifyUserTagPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'VerifyUserTags'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'verify-user-tag/:id/delete',
        component: VerifyUserTagDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'VerifyUserTags'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

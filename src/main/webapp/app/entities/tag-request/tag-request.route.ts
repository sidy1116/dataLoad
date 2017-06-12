import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { TagRequestComponent } from './tag-request.component';
import { TagRequestDetailComponent } from './tag-request-detail.component';
import { TagRequestPopupComponent } from './tag-request-dialog.component';
import { TagRequestDeletePopupComponent } from './tag-request-delete-dialog.component';

import { Principal } from '../../shared';

export const tagRequestRoute: Routes = [
    {
        path: 'tag-request',
        component: TagRequestComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TagRequests'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'tag-request/:id',
        component: TagRequestDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TagRequests'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const tagRequestPopupRoute: Routes = [
    {
        path: 'tag-request-new',
        component: TagRequestPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TagRequests'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tag-request/:id/edit',
        component: TagRequestPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TagRequests'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tag-request/:id/delete',
        component: TagRequestDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TagRequests'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

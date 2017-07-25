import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { RadsCategoryInventory } from './rads-category-inventory.model';
import { RadsCategoryInventoryPopupService } from './rads-category-inventory-popup.service';
import { RadsCategoryInventoryService } from './rads-category-inventory.service';

@Component({
    selector: 'jhi-rads-category-inventory-dialog',
    templateUrl: './rads-category-inventory-dialog.component.html'
})
export class RadsCategoryInventoryDialogComponent implements OnInit {

    radsCategoryInventory: RadsCategoryInventory;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private radsCategoryInventoryService: RadsCategoryInventoryService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.radsCategoryInventory.id !== undefined) {
            this.subscribeToSaveResponse(
                this.radsCategoryInventoryService.update(this.radsCategoryInventory), false);
        } else {
            this.subscribeToSaveResponse(
                this.radsCategoryInventoryService.create(this.radsCategoryInventory), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<RadsCategoryInventory>, isCreated: boolean) {
        result.subscribe((res: RadsCategoryInventory) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: RadsCategoryInventory, isCreated: boolean) {
        this.alertService.success(
            isCreated ? `A new Rads Category Inventory is created with identifier ${result.id}`
            : `A Rads Category Inventory is updated with identifier ${result.id}`,
            null, null);

        this.eventManager.broadcast({ name: 'radsCategoryInventoryListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-rads-category-inventory-popup',
    template: ''
})
export class RadsCategoryInventoryPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private radsCategoryInventoryPopupService: RadsCategoryInventoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.radsCategoryInventoryPopupService
                    .open(RadsCategoryInventoryDialogComponent, params['id']);
            } else {
                this.modalRef = this.radsCategoryInventoryPopupService
                    .open(RadsCategoryInventoryDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

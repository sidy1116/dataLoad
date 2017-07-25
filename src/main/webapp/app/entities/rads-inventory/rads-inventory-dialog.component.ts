import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { RadsInventory } from './rads-inventory.model';
import { RadsInventoryPopupService } from './rads-inventory-popup.service';
import { RadsInventoryService } from './rads-inventory.service';

@Component({
    selector: 'jhi-rads-inventory-dialog',
    templateUrl: './rads-inventory-dialog.component.html'
})
export class RadsInventoryDialogComponent implements OnInit {

    radsInventory: RadsInventory;
    authorities: any[];
    isSaving: boolean;
    inventoryDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private radsInventoryService: RadsInventoryService,
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
        if (this.radsInventory.id !== undefined) {
            this.subscribeToSaveResponse(
                this.radsInventoryService.update(this.radsInventory), false);
        } else {
            this.subscribeToSaveResponse(
                this.radsInventoryService.create(this.radsInventory), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<RadsInventory>, isCreated: boolean) {
        result.subscribe((res: RadsInventory) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: RadsInventory, isCreated: boolean) {
        this.alertService.success(
            isCreated ? `A new Rads Inventory is created with identifier ${result.id}`
            : `A Rads Inventory is updated with identifier ${result.id}`,
            null, null);

        this.eventManager.broadcast({ name: 'radsInventoryListModification', content: 'OK'});
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
    selector: 'jhi-rads-inventory-popup',
    template: ''
})
export class RadsInventoryPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private radsInventoryPopupService: RadsInventoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.radsInventoryPopupService
                    .open(RadsInventoryDialogComponent, params['id']);
            } else {
                this.modalRef = this.radsInventoryPopupService
                    .open(RadsInventoryDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

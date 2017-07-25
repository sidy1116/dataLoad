import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { RadsInventory } from './rads-inventory.model';
import { RadsInventoryPopupService } from './rads-inventory-popup.service';
import { RadsInventoryService } from './rads-inventory.service';

@Component({
    selector: 'jhi-rads-inventory-delete-dialog',
    templateUrl: './rads-inventory-delete-dialog.component.html'
})
export class RadsInventoryDeleteDialogComponent {

    radsInventory: RadsInventory;

    constructor(
        private radsInventoryService: RadsInventoryService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.radsInventoryService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'radsInventoryListModification',
                content: 'Deleted an radsInventory'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success(`A Rads Inventory is deleted with identifier ${id}`, null, null);
    }
}

@Component({
    selector: 'jhi-rads-inventory-delete-popup',
    template: ''
})
export class RadsInventoryDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private radsInventoryPopupService: RadsInventoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.radsInventoryPopupService
                .open(RadsInventoryDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

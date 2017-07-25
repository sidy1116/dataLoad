import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { RadsCategoryInventory } from './rads-category-inventory.model';
import { RadsCategoryInventoryPopupService } from './rads-category-inventory-popup.service';
import { RadsCategoryInventoryService } from './rads-category-inventory.service';

@Component({
    selector: 'jhi-rads-category-inventory-delete-dialog',
    templateUrl: './rads-category-inventory-delete-dialog.component.html'
})
export class RadsCategoryInventoryDeleteDialogComponent {

    radsCategoryInventory: RadsCategoryInventory;

    constructor(
        private radsCategoryInventoryService: RadsCategoryInventoryService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.radsCategoryInventoryService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'radsCategoryInventoryListModification',
                content: 'Deleted an radsCategoryInventory'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success(`A Rads Category Inventory is deleted with identifier ${id}`, null, null);
    }
}

@Component({
    selector: 'jhi-rads-category-inventory-delete-popup',
    template: ''
})
export class RadsCategoryInventoryDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private radsCategoryInventoryPopupService: RadsCategoryInventoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.radsCategoryInventoryPopupService
                .open(RadsCategoryInventoryDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

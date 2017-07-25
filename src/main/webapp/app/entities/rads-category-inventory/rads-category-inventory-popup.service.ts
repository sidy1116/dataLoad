import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { RadsCategoryInventory } from './rads-category-inventory.model';
import { RadsCategoryInventoryService } from './rads-category-inventory.service';

@Injectable()
export class RadsCategoryInventoryPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private radsCategoryInventoryService: RadsCategoryInventoryService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.radsCategoryInventoryService.find(id).subscribe((radsCategoryInventory) => {
                this.radsCategoryInventoryModalRef(component, radsCategoryInventory);
            });
        } else {
            return this.radsCategoryInventoryModalRef(component, new RadsCategoryInventory());
        }
    }

    radsCategoryInventoryModalRef(component: Component, radsCategoryInventory: RadsCategoryInventory): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.radsCategoryInventory = radsCategoryInventory;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}

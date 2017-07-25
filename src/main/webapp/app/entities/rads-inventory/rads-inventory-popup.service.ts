import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { RadsInventory } from './rads-inventory.model';
import { RadsInventoryService } from './rads-inventory.service';

@Injectable()
export class RadsInventoryPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private radsInventoryService: RadsInventoryService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.radsInventoryService.find(id).subscribe((radsInventory) => {
                if (radsInventory.inventoryDate) {
                    radsInventory.inventoryDate = {
                        year: radsInventory.inventoryDate.getFullYear(),
                        month: radsInventory.inventoryDate.getMonth() + 1,
                        day: radsInventory.inventoryDate.getDate()
                    };
                }
                this.radsInventoryModalRef(component, radsInventory);
            });
        } else {
            return this.radsInventoryModalRef(component, new RadsInventory());
        }
    }

    radsInventoryModalRef(component: Component, radsInventory: RadsInventory): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.radsInventory = radsInventory;
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

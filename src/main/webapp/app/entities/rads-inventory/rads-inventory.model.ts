export class RadsInventory {
    constructor(
        public id?: number,
        public catId?: number,
        public catName?: string,
        public inventoryDate?: any,
        public count?: number,
        public prevInvCount?: number,
        public diffPercentage?: string,
    ) {
    }
}

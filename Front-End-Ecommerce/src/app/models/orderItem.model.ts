export class OrderItem {
    id: number;
    productId: number;
    productName: string;
    quantity: number;
    price: number;
    productImgUrl: string;

    constructor(id: number, productId: number, productName: string, quantity: number, price: number, productImgUrl: string) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.productImgUrl = productImgUrl;
    }
}
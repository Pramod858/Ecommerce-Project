import { Address } from "./address.model";
import { OrderStatus } from "./order-status.enum";
import { OrderItem } from "./orderItem.model";

export class Order {
    id: number;
    userId: number;
    orderDate: Date;
    totalPrice: number;
    orderItems: OrderItem[];
    status: OrderStatus;
    shippingAddress: Address;

    constructor(id: number, userId: number, orderDate: Date, totalPrice: number, orderItems: OrderItem[], status: OrderStatus, shippingAddress: Address) {
        this.id = id;
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.orderItems = orderItems;
        this.status = status;
        this.shippingAddress = shippingAddress;
    }
}
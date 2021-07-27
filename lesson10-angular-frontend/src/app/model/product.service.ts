import {Injectable} from '@angular/core';
import {Product} from "./product";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private identity: number = 6;

  private products: { [key: number]: Product } = {
    1: new Product(1, '1 product', 234),
    2: new Product(2, '2 product', 212),
    3: new Product(3, '3 product', 424),
    4: new Product(4, '4 product', 534),
    5: new Product(5, '5 product', 643),
  };

  constructor(public http: HttpClient) { }

  public findAll() {
    return this.http.get<Product[]>('/api/v1/product/all').toPromise();
    // return new Promise<Product[]>((resolve, reject) =>
    // {
    //   resolve(
    //     Object.values(this.products)
    //   )
    // })
  }

  public findById(id: number) {
    return this.http.get<Product>(`/api/v1/product/${id}`).toPromise();
    // return new Promise<Product>((resolve, reject) =>
    // {
    //   resolve(
    //     this.products[id]
    //   )
    // })
  }

  public save(product: Product) {
    return this.http.put(`/api/v1/product`, product).toPromise();
    // return new Promise<void>((resolve, reject) =>
    // {
    //   if (product.id == -1) {
    //     product.id = this.identity++;
    //   }
    //   this.products[product.id] = product;
    //   resolve();
    // })
  }

  public delete(id: number) {
    return this.http.delete(`/api/v1/product/${id}`).toPromise();
    // return new Promise<void>((resolve, reject) =>
    // {
    //   delete this.products[id];
    //   resolve();
    // })
  }
}

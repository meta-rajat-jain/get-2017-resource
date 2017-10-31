import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from '../home/home.component';





const routes: Routes = [
  { path: '', component:HomeComponent}
  /*,
  { path: 'dashboard',  component: DashboardComponent },
  { path: 'detail/:id', component: ProductDetailComponent },
  { path: 'productsList', component: ProductsComponent },
  { path: 'contactUs', component: ContactUsComponent },
  { path: 'addProduct', component: AddProductComponent },
  { path: 'getProductDetail/:id', component: GetProductDetailComponent },
  { path: 'cart', component: Cart },
  { path: 'checkout', component: Checkout },
  { path: 'payment', component: PaymentComponent },
  { path: 'order', component:OrderComponent},
  { path: 'orderDetail/:orderId', component:OrderDetailComponent}*/

];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}

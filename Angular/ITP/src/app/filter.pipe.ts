import { Component, OnInit } from '@angular/core';

import { Pipe, PipeTransform } from '@angular/core';
@Pipe({
  name: 'filter'
})
export class FilterPipe implements PipeTransform {
 
   transform(items: any[], searchText): any {
    
    return searchText 
        ? items.filter(item => (item.login.username.toLowerCase().indexOf(searchText) !== -1 ||
                                item.name.indexOf(searchText) !== -1 ||
                                item.designation.toLowerCase().indexOf(searchText) !== -1) )
        : items;
}
}
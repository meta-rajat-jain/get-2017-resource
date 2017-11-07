import { Component, OnInit } from '@angular/core';

import { Pipe, PipeTransform } from '@angular/core';
@Pipe({
  name: 'filter'
})
export class FilterPipe implements PipeTransform {
 /* transform(items: any[], searchText: string): any[] {
    if(!items) return [];
    if(!searchText) return items;

return items.filter( item => {
    console.log("in filter" + item.toLowerCase().includes(searchText));
      return item.toLowerCase().includes(searchText);
    });
   }*/
   transform(items: any[], searchText): any {
    console.log('term', searchText);
  
    return searchText 
        ? items.filter(item => item.name.toLowerCase().indexOf(searchText) !== -1)
        : items;
}
}
import { RouteReuseStrategy, DefaultUrlSerializer, ActivatedRouteSnapshot, DetachedRouteHandle } from '@angular/router';

export class SimpleReuseStrategy implements RouteReuseStrategy {

    _cacheRouters: { [key: string]: any } = {};

    // shouldDetach 直接返回 true 表示对所有路由允许复用
    shouldDetach(route: ActivatedRouteSnapshot): boolean {
        return route.data.keep
    }
    // store 当路由离开时会触发。按path作为key存储路由快照&组件当前实例对象；path等同RouterModule.forRoot中的配置。
    store(route: ActivatedRouteSnapshot, handle: DetachedRouteHandle): void {
        let key = this.hashRoute(route)
        this._cacheRouters[key] = {
            snapshot: route,
            handle: handle
        };
    }
    // shouldAttach 若 path 在缓存中有的都认为允许还原路由
    shouldAttach(route: ActivatedRouteSnapshot): boolean {
        let key = this.hashRoute(route)
        return !!this._cacheRouters[key];
    }
    // retrieve 从缓存中获取快照，若无则返回null
    retrieve(route: ActivatedRouteSnapshot): DetachedRouteHandle {
        let key = this.hashRoute(route)
        let cacheRouter = this._cacheRouters[key];
        return cacheRouter ? cacheRouter.handle : null;
    }
    // shouldReuseRoute 进入路由触发，是否同一路由时复用路由
    shouldReuseRoute(future: ActivatedRouteSnapshot, curr: ActivatedRouteSnapshot): boolean {
        return future.routeConfig === curr.routeConfig;
    }

    // 简单的hash route
    hashRoute(route: ActivatedRouteSnapshot): string {
        let t1 = new Date().getTime()
        // 这里小心JSON循环转换问题，避免出现路由参数存在循环引用的情况
        let text = route.routeConfig.path
            + '|'
            + JSON.stringify(route.params)
            + '|'
            + JSON.stringify(route.queryParams)

        let key = this.hashText(text);
        let t2 = new Date().getTime()
        
        // console.log(`hashRoute------------------------->`)
        // console.log(`t2-t1: ${t2 - t1}`)
        // console.log(`text: ${text}`)
        // console.log(`key: ${key}`)
        return key;
    }

    I64BIT_TABLE = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-'.split('');

    // 根据整个字符串计算哈希值
    // ..网上找的hash算法，希望不会重复
    hashText(input: string): string {
        var hash = 5381;
        var i = input.length - 1;

        if (typeof input == 'string') {
            for (; i > -1; i--)
                hash += (hash << 5) + input.charCodeAt(i);
        }
        else {
            for (; i > -1; i--)
                hash += (hash << 5) + input[i];
        }
        var value = hash & 0x7FFFFFFF;

        var retValue = '';
        do {
            retValue += this.I64BIT_TABLE[value & 0x3F];
        }
        while (value >>= 6);

        return retValue;
    }
}
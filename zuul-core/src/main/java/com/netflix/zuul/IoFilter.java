/**
 * Copyright 2014 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.zuul;

import rx.Observable;

/**
 * Filter which does IO work, and returns Observable values
 * @param <T> input type
 */
public abstract class IoFilter<T> implements ProcessingFilter<T> {
    public abstract Observable<Boolean> shouldFilter(T input);
    public abstract Observable<T> apply(T input);

    @Override
    public Observable<T> execute(Observable<T> input) {
        return input.flatMap(t -> shouldFilter(t).flatMap(shouldFilter -> {
            if (shouldFilter) {
                return apply(t);
            } else {
                return input;
            }
        }));
    }
}
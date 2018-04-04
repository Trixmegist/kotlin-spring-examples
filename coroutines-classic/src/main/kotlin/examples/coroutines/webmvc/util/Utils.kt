/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package examples.coroutines.webmvc.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.full.companionObject

inline fun <reified R : Any> R.logger(): Logger = LoggerFactory.getLogger(unwrapCompanionClass(R::class.java))

fun <T : Any> unwrapCompanionClass(ofClass: Class<T>): Class<*> =
        if (ofClass.enclosingClass != null && ofClass.enclosingClass.kotlin.companionObject?.java == ofClass)
            ofClass.enclosingClass
        else ofClass


inline val <reified T : Any> T.log: Logger
    get() = LoggerFactory.getLogger(T::class.java)
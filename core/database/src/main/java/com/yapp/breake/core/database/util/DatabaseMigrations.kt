/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yapp.breake.core.database.util

/**
 * 자동 스키마 마이그레이션은 때때로 마이그레이션을 수행하기 위해 추가 지침이 필요할 수 있습니다.
 * 예를 들어, 열의 이름이 변경되는 경우와 같은 상황이 이에 해당합니다.
 * 이러한 추가 지침은 `SchemaXtoY`라는 명명 규칙을 사용하여 클래스를 생성함으로써 여기에 배치됩니다.
 * 여기서 X는 마이그레이션할 스키마 버전이며, Y는 마이그레이션할 대상 스키마 버전입니다.
 * 이 클래스는 `AutoMigrationSpec`을 구현해야 합니다.
 */
internal object DatabaseMigrations

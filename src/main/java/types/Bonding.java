/*
Copyright (c) 2015 Red Hat, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package types;

import org.ovirt.api.metamodel.annotations.Type;
import org.ovirt.api.metamodel.annotations.Link;

/**
 * Represents a network interfaces bond.
 *
 * @author Leon Goldberg <lgoldber@redhat.com>
 * @date 12 Dec 2016
 * @status added
 */
@Type
public interface Bonding {
    Option[] options();
    HostNic[] slaves();

    /**
     * The `active_slave` property of the bond in modes that support it (active-backup, balance-alb and balance-tlb).
     * See https://www.kernel.org/doc/Documentation/networking/bonding.txt[Linux documentation] for further details.
     * This parameter is read-only. Setting it will have no effect on the bond.
     * It is retrieved from `/sys/class/net/bondX/bonding/active_slave` file on the system where the bond is located.
     *
     * For example:
     *
     * [source]
     * ----
     * GET /ovirt-engine/api/hosts/123/nics/321
     * ----
     *
     * Will respond:
     *
     * [source,xml]
     * ----
     * <host_nic href="/ovirt-engine/api/hosts/123/nics/321" id="321">
     *   ...
     *   <bonding>
     *     <slaves>
     *       <host_nic href="/ovirt-engine/api/hosts/123/nics/456" id="456" />
     *       ...
     *     </slaves>
     *     <active_slave href="/ovirt-engine/api/hosts/123/nics/456" id="456" />
     *   </bonding>
     *   ...
     * </host_nic>
     * ----
     *
     * @author Dominik Holler <dholler@redhat.com>
     * @author Megan Lewis <melewis@redhat.com>
     * @date 2 Dec 2016
     * @status updated_by_docs
     * @since 4.1.0
     */
    @Link HostNic activeSlave();

    /**
     * The `ad_partner_mac` property of the partner bond in mode 4. Bond mode 4 is the 802.3ad standard, which is
     * also called dynamic link aggregation. See
     * https://en.wikipedia.org/wiki/Link_aggregation[Wikipedia] and
     * http://www.ieee802.org/3/hssg/public/apr07/frazier_01_0407.pdf[Presentation] for more information.
     * `ad_partner_mac` is the MAC address of the system (switch) at the other end of a bond.
     * This parameter is read-only. Setting it will have no effect on the bond.
     * It is retrieved from `/sys/class/net/bondX/bonding/ad_partner_mac` file on the system where the bond is located.
     *
     * @author Marcin Mirecki <mmirecki@redhat.com>
     * @author Megan Lewis <melewis@redhat.com>
     * @date 2 Dec 2016
     * @status updated_by_docs
     * @since 4.0.3
     */
    Mac adPartnerMac();
}

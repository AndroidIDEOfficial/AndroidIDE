/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * reserved comment block
 * DO NOT REMOVE OR ALTER!
 */
/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * $Id: SerializerMessages_pt_BR.java /st_wptg_1.8.0.0.0jdk/2 2013/09/11 12:46:54 gmolloy Exp $
 */
package jaxp.sun.org.apache.xml.internal.serializer.utils;

import java.util.ListResourceBundle;

/**
 * An instance of this class is a ListResourceBundle that
 * has the required getContents() method that returns
 * an array of message-key/message associations.
 * <p>
 * The message keys are defined in {@link MsgKey}. The
 * messages that those keys map to are defined here.
 * <p>
 * The messages in the English version are intended to be
 * translated.
 *
 * This class is not a public API, it is only public because it is
 * used in com.sun.org.apache.xml.internal.serializer.
 *
 * @xsl.usage internal
 */
public class SerializerMessages_pt_BR extends ListResourceBundle {

    /*
     * This file contains error and warning messages related to
     * Serializer Error Handling.
     *
     *  General notes to translators:

     *  1) A stylesheet is a description of how to transform an input XML document
     *     into a resultant XML document (or HTML document or text).  The
     *     stylesheet itself is described in the form of an XML document.

     *
     *  2) An element is a mark-up tag in an XML document; an attribute is a
     *     modifier on the tag.  For example, in <elem attr='val' attr2='val2'>
     *     "elem" is an element name, "attr" and "attr2" are attribute names with
     *     the values "val" and "val2", respectively.
     *
     *  3) A namespace declaration is a special attribute that is used to associate
     *     a prefix with a URI (the namespace).  The meanings of element names and
     *     attribute names that use that prefix are defined with respect to that
     *     namespace.
     *
     *
     */

    /** The lookup table for error messages.   */
    public Object[][] getContents() {
        Object[][] contents = new Object[][] {
            {   MsgKey.BAD_MSGKEY,
                "A chave de mensagem ''{0}'' n\u00E3o est\u00E1 na classe de mensagem ''{1}''" },

            {   MsgKey.BAD_MSGFORMAT,
                "Houve falha no formato da mensagem ''{0}'' na classe de mensagem ''{1}''." },

            {   MsgKey.ER_SERIALIZER_NOT_CONTENTHANDLER,
                "A classe ''{0}'' do serializador n\u00E3o implementa org.xml.sax.ContentHandler." },

            {   MsgKey.ER_RESOURCE_COULD_NOT_FIND,
                    "N\u00E3o foi poss\u00EDvel encontrar o recurso [ {0} ].\n {1}" },

            {   MsgKey.ER_RESOURCE_COULD_NOT_LOAD,
                    "O recurso [ {0} ] n\u00E3o foi carregado: {1} \n {2} \t {3}" },

            {   MsgKey.ER_BUFFER_SIZE_LESSTHAN_ZERO,
                    "Tamanho do buffer <=0" },

            {   MsgKey.ER_INVALID_UTF16_SURROGATE,
                    "Foi detectado um substituto de UTF-16 inv\u00E1lido: {0} ?" },

            {   MsgKey.ER_OIERROR,
                "Erro de E/S" },

            {   MsgKey.ER_ILLEGAL_ATTRIBUTE_POSITION,
                "N\u00E3o \u00E9 poss\u00EDvel adicionar o atributo {0} depois dos n\u00F3s filhos ou antes que um elemento seja produzido. O atributo ser\u00E1 ignorado." },

            /*
             * Note to translators:  The stylesheet contained a reference to a
             * namespace prefix that was undefined.  The value of the substitution
             * text is the name of the prefix.
             */
            {   MsgKey.ER_NAMESPACE_PREFIX,
                "O namespace do prefixo ''{0}'' n\u00E3o foi declarado." },

            /*
             * Note to translators:  This message is reported if the stylesheet
             * being processed attempted to construct an XML document with an
             * attribute in a place other than on an element.  The substitution text
             * specifies the name of the attribute.
             */
            {   MsgKey.ER_STRAY_ATTRIBUTE,
                "Atributo ''{0}'' fora do elemento." },

            /*
             * Note to translators:  As with the preceding message, a namespace
             * declaration has the form of an attribute and is only permitted to
             * appear on an element.  The substitution text {0} is the namespace
             * prefix and {1} is the URI that was being used in the erroneous
             * namespace declaration.
             */
            {   MsgKey.ER_STRAY_NAMESPACE,
                "Declara\u00E7\u00E3o de namespace ''{0}''=''{1}'' fora do elemento." },

            {   MsgKey.ER_COULD_NOT_LOAD_RESOURCE,
                "N\u00E3o foi poss\u00EDvel carregar ''{0}'' (verificar CLASSPATH); usando agora apenas os defaults" },

            {   MsgKey.ER_ILLEGAL_CHARACTER,
                "Tentativa de exibir um caractere de valor integral {0} que n\u00E3o est\u00E1 representado na codifica\u00E7\u00E3o de sa\u00EDda especificada de {1}." },

            {   MsgKey.ER_COULD_NOT_LOAD_METHOD_PROPERTY,
                "N\u00E3o foi poss\u00EDvel carregar o arquivo de propriedade ''{0}'' para o m\u00E9todo de sa\u00EDda ''{1}'' (verificar CLASSPATH)" },

            {   MsgKey.ER_INVALID_PORT,
                "N\u00FAmero de porta inv\u00E1lido" },

            {   MsgKey.ER_PORT_WHEN_HOST_NULL,
                "A porta n\u00E3o pode ser definida quando o host \u00E9 nulo" },

            {   MsgKey.ER_HOST_ADDRESS_NOT_WELLFORMED,
                "O host n\u00E3o \u00E9 um endere\u00E7o correto" },

            {   MsgKey.ER_SCHEME_NOT_CONFORMANT,
                "O esquema n\u00E3o \u00E9 compat\u00EDvel." },

            {   MsgKey.ER_SCHEME_FROM_NULL_STRING,
                "N\u00E3o \u00E9 poss\u00EDvel definir o esquema de uma string nula" },

            {   MsgKey.ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE,
                "O caminho cont\u00E9m uma sequ\u00EAncia inv\u00E1lida de caracteres de escape" },

            {   MsgKey.ER_PATH_INVALID_CHAR,
                "O caminho cont\u00E9m um caractere inv\u00E1lido: {0}" },

            {   MsgKey.ER_FRAG_INVALID_CHAR,
                "O fragmento cont\u00E9m um caractere inv\u00E1lido" },

            {   MsgKey.ER_FRAG_WHEN_PATH_NULL,
                "O fragmento n\u00E3o pode ser definido quando o caminho \u00E9 nulo" },

            {   MsgKey.ER_FRAG_FOR_GENERIC_URI,
                "O fragmento s\u00F3 pode ser definido para um URI gen\u00E9rico" },

            {   MsgKey.ER_NO_SCHEME_IN_URI,
                "Nenhum esquema encontrado no URI" },

            {   MsgKey.ER_CANNOT_INIT_URI_EMPTY_PARMS,
                "N\u00E3o \u00E9 poss\u00EDvel inicializar o URI com par\u00E2metros vazios" },

            {   MsgKey.ER_NO_FRAGMENT_STRING_IN_PATH,
                "O fragmento n\u00E3o pode ser especificado no caminho nem no fragmento" },

            {   MsgKey.ER_NO_QUERY_STRING_IN_PATH,
                "A string de consulta n\u00E3o pode ser especificada no caminho nem na string de consulta" },

            {   MsgKey.ER_NO_PORT_IF_NO_HOST,
                "A porta n\u00E3o pode ser especificada se o host n\u00E3o tiver sido especificado" },

            {   MsgKey.ER_NO_USERINFO_IF_NO_HOST,
                "As informa\u00E7\u00F5es do usu\u00E1rio n\u00E3o podem ser especificadas se o host n\u00E3o tiver sido especificado" },

            {   MsgKey.ER_XML_VERSION_NOT_SUPPORTED,
                "Advert\u00EAncia: a vers\u00E3o do documento de sa\u00EDda deve ser obrigatoriamente ''{0}''. Esta vers\u00E3o do XML n\u00E3o \u00E9 suportada. A vers\u00E3o do documento de sa\u00EDda ser\u00E1 ''1.0''." },

            {   MsgKey.ER_SCHEME_REQUIRED,
                "O esquema \u00E9 obrigat\u00F3rio!" },

            /*
             * Note to translators:  The words 'Properties' and
             * 'SerializerFactory' in this message are Java class names
             * and should not be translated.
             */
            {   MsgKey.ER_FACTORY_PROPERTY_MISSING,
                "O objeto Properties especificado para a SerializerFactory n\u00E3o tem uma propriedade ''{0}''." },

            {   MsgKey.ER_ENCODING_NOT_SUPPORTED,
                "Advert\u00EAncia: a codifica\u00E7\u00E3o ''{0}'' n\u00E3o \u00E9 suportada pelo Java runtime." },


        };

        return contents;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.daniejunior.springmvclearn.services;

import br.com.daniejunior.springmvclearn.domain.Product;
import java.util.List;

/**
 *
 * @author danieljr
 */
public interface ProductService {
    List<Product> listAllProducts();
}

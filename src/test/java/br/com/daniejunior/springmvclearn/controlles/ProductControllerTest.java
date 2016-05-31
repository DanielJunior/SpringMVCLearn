/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.daniejunior.springmvclearn.controlles;

import br.com.daniejunior.springmvclearn.controllers.ProductController;
import br.com.daniejunior.springmvclearn.domain.Product;
import br.com.daniejunior.springmvclearn.services.ProductService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matchers;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;

/**
 *
 * @author danieljr
 */
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    //here don't care how about i will fit the productService variable, because using
    //initMocks at setup, I will be able to inject the above instance annotatw with @Mock
    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public void testList() throws Exception {
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());

        Mockito.when(productService.listAllProducts()).thenReturn(products);

        mockMvc.perform(MockMvcRequestBuilders.get("/product/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("product/list"))
                .andExpect(MockMvcResultMatchers.model().attribute("products", Matchers.hasSize(2)));
    }

    @Test
    public void testShow() throws Exception {
        Integer id = 1;
        Mockito.when(productService.getProductById(id)).thenReturn(new Product());

        mockMvc.perform(MockMvcRequestBuilders.get("/product/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("product/show"))
                .andExpect(MockMvcResultMatchers.model().attribute("product", Matchers.instanceOf(Product.class)));
    }

    @Test
    public void testEdit() throws Exception {
        Integer id = 1;
        Mockito.when(productService.getProductById(id)).thenReturn(new Product());

        mockMvc.perform(MockMvcRequestBuilders.get("/product/edit/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("product/productform"))
                .andExpect(MockMvcResultMatchers.model().attribute("product", Matchers.instanceOf(Product.class)));
    }

    @Test
    public void testNewProduct() throws Exception {
        Mockito.verifyZeroInteractions(productService);

        mockMvc.perform(MockMvcRequestBuilders.get("/product/new"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("product/productform"))
                .andExpect(MockMvcResultMatchers.model().attribute("product", Matchers.instanceOf(Product.class)));

    }

    @Test
    public void testSaveOrUpdate() throws Exception {
        Integer id = 1;
        String description = "Product 1";
        BigDecimal price = new BigDecimal("12.99");
        String imageUrl = "http://example.com/product1";

        Product product = new Product();
        product.setId(id);
        product.setDescription(description);
        product.setPrice(price);
        product.setImageUrl(imageUrl);

        Mockito.when(productService.saveOrUpdateProduct(Mockito.<Product>any())).thenReturn(product);
        mockMvc.perform(MockMvcRequestBuilders.post("/product")
                .param("id", "1")
                .param("description", description)
                .param("price", "12.99")
                .param("imageUrl", imageUrl))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/product/1"))
                .andExpect(MockMvcResultMatchers.model().attribute("product", Matchers.instanceOf(Product.class)))
                .andExpect(MockMvcResultMatchers.model().attribute("product", Matchers.hasProperty("id", Matchers.is(id))))
                .andExpect(MockMvcResultMatchers.model().attribute("product", Matchers.hasProperty("description", Matchers.is(description))))
                .andExpect(MockMvcResultMatchers.model().attribute("product", Matchers.hasProperty("price", Matchers.is(price))))
                .andExpect(MockMvcResultMatchers.model().attribute("product", Matchers.hasProperty("imageUrl", Matchers.is(imageUrl))));
        
// verify properties of bound object
        ArgumentCaptor<Product> boundProduct = ArgumentCaptor.forClass(Product.class);
        verify(productService).saveOrUpdateProduct(boundProduct.capture());

        assertEquals(id, boundProduct.getValue().getId());
        assertEquals(description, boundProduct.getValue().getDescription());
        assertEquals(price, boundProduct.getValue().getPrice());
        assertEquals(imageUrl, boundProduct.getValue().getImageUrl());
    }
    
    @Test
    public void testDelete() throws Exception{
        Integer id = 1;
        mockMvc.perform(MockMvcRequestBuilders.get("/product/delete/1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/product/list"));
        verify(productService, times(1)).delete(id);
    }
}
